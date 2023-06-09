package handler

import (
	"fmt"
	"sync"

	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/pkg/events"
	"github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/pkg/kafka"
)

type UpdateBalanceKafkaHandler struct {
	Kafka *kafka.Producer
}

func NewUpdateBalanceKafkaHandler(kafka *kafka.Producer) *UpdateBalanceKafkaHandler {
	return &UpdateBalanceKafkaHandler{
		Kafka: kafka,
	}
}

func (h *UpdateBalanceKafkaHandler) Handle(message events.EventInterface, wg *sync.WaitGroup) {
	defer wg.Done()
	h.Kafka.Publish(message.GetPayload(), nil, "balances")
	fmt.Println("UpdateBalanceKafkaHandler called")
}
