package gateway

import "github.com.br/luisbilecki/fullcycle-eda/internal/entity"

type TransactionGateway interface {
	Create(transaction *entity.Transaction) error
}
