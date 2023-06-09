package main

import (
	"context"
	"database/sql"
	"fmt"

	"github.com/golang-migrate/migrate/v4"
	mysqlMigrate "github.com/golang-migrate/migrate/v4/database/mysql"

	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/database"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/event"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/event/handler"
	createaccount "github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/usecase/create_account"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/usecase/create_client"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/usecase/create_transaction"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/web"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/web/webserver"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/pkg/events"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/pkg/kafka"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/pkg/uow"
	ckafka "github.com/confluentinc/confluent-kafka-go/kafka"
	_ "github.com/go-sql-driver/mysql"
	_ "github.com/golang-migrate/migrate/v4/source/file"
)

func main() {
	db, err := sql.Open("mysql", fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8&parseTime=True&loc=Local", "wallet", "password", "wallet-db", "3306", "wallet"))
	if err != nil {
		panic(err)
	}
	defer db.Close()

	migrateDatabase(db)

	configMap := ckafka.ConfigMap{
		"bootstrap.servers": "kafka:9092",
		"group.id":          "wallet",
	}
	kafkaProducer := kafka.NewKafkaProducer(&configMap)

	eventDispatcher := events.NewEventDispatcher()
	eventDispatcher.Register("TransactionCreated", handler.NewTransactionCreatedKafkaHandler(kafkaProducer))
	eventDispatcher.Register("BalanceUpdated", handler.NewUpdateBalanceKafkaHandler(kafkaProducer))

	transactionCreatedEvent := event.NewTransactionCreated()
	balanceUpdatedEvent := event.NewBalanceUpdated()

	clientDb := database.NewClientDB(db)
	accountDb := database.NewAccountDB(db)

	ctx := context.Background()
	uow := uow.NewUow(ctx, db)

	uow.Register("AccountDB", func(tx *sql.Tx) interface{} {
		return database.NewAccountDB(db)
	})

	uow.Register("TransactionDB", func(tx *sql.Tx) interface{} {
		return database.NewTransactionDB(db)
	})
	createTransactionUseCase := create_transaction.NewCreateTransactionUseCase(uow, eventDispatcher, transactionCreatedEvent, balanceUpdatedEvent)
	createClientUseCase := create_client.NewCreateClientUseCase(clientDb)
	createAccountUseCase := createaccount.NewCreateAccountUseCase(accountDb, clientDb)

	webserver := webserver.NewWebServer(":8080")

	clientHandler := web.NewWebClientHandler(*createClientUseCase)
	accountHandler := web.NewWebAccountHandler(*createAccountUseCase)
	transactionHandler := web.NewWebTransactionHandler(*createTransactionUseCase)

	webserver.AddHandler("/clients", clientHandler.CreateClient)
	webserver.AddHandler("/accounts", accountHandler.CreateAccount)
	webserver.AddHandler("/transactions", transactionHandler.CreateTransaction)

	fmt.Println("Server is running")
	webserver.Start()
}

func migrateDatabase(db *sql.DB) {
	driver, err := mysqlMigrate.WithInstance(db, &mysqlMigrate.Config{})
	if err != nil {
		panic(err)
	}
	m, err := migrate.NewWithDatabaseInstance(
		"file://migrations",
		"wallet", driver)
	if err != nil {
		panic(err)
	}
	m.Up()
}
