package gateway

import "github.com.br/luisbilecki/desafio-eda-fullcycle/ms-wallet-core/internal/entity"

type TransactionGateway interface {
	Create(transaction *entity.Transaction) error
}
