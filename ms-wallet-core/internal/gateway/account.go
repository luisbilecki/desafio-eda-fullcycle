package gateway

import "github.com.br/luisbilecki/fullcycle-eda/internal/entity"

type AccountGateway interface {
	Save(account *entity.Account) error
	FindByID(id string) (*entity.Account, error)
	UpdateBalance(account *entity.Account) error
}
