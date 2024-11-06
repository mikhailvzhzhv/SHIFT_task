# CRM for SHIFT LAB

## API

## Продавцы (Sellers)

### 1. Создать нового продавца
#### POST /sellers/

Тело запроса:

json
```
{
    "name": "Mikhail",
    "contact_info": "mvzhzhv@gmail.com"
}
```
Ответ:

json
```
{
    "id": 1,
    "name": "Mikhail",
    "contact_info": "mvzhzhv@gmail.com",
    "registration_date": "2024-10-20T12:34:56"
}
```

### 2. Получить список всех продавцов
#### GET /sellers/

Ответ:

json
```
[
    {
        "id": 1,
        "name": "Mikhail",
        "contact_info": "mvzhzhv@gmail.com",
        "registration_date": "2024-10-20T12:34:56"
    }
]
```

### 3. Обновить информацию о продавце
#### PATCH /sellers/{id}

Тело запроса:

json
```
{
    "name": "Sanya",
    "contact_info": "sanya@gmail.com"
}
```

Ответ:

json
```
{
    "id": 1,
    "name": "Sanya",
    "contact_info": "sanya@gmail.com",
    "registration_date": "2024-10-20T12:34:56"
}
```

Если id не существует:

json
```
{
    "message": string
}
```

### 4. Удалить продавца
#### DELETE /sellers/{id}

Ответ:

json
```
{
    "id": 1,
    "name": "Mikhail",
    "contact_info": "mvzhzhv@gmail.com",
    "registration_date": "2024-10-20T12:34:56"
}
```
Если id не существует:

json
```
{
    "message": string
}
```

### 5. Получить транзакции продавца
#### GET /sellers/{id}/transactions

Ответ:

json
```
[
    {
        "id": 1,
        "seller_id": 1,
        "amount": 10000,
        "payment_type": "CASH",
        "transaction_date": "2024-10-20T12:34:56"
    }
]
```

Если id не существует:

json
```
{
    "message": string
}
```


## Транзакции (Transactions)

### 1. Создать транзакцию
#### POST /transactions/

Тело запроса:

json
```
{
    "seller_id": 1,
    "amount": 10000,
    "payment_type": "CARD"
}
```
Ответ:

json
```
{
    "id": 1,
    "seller_id": 1,
    "amount": 10000,
    "payment_type": "CARD",
    "transaction_date": "2024-10-20T12:34:56"
}
```

### 2. Получить все транзакции
#### GET /transactions/

Ответ:

json
```
[
    {
        "id": 1,
        "seller_id": 1,
        "amount": 10000,
        "payment_type": "CARD",
        "transaction_date": "2024-10-20T12:34:56"
    }
]
```

### 3. Получить информацию о конкретной транзакции
#### GET /transactions/{id}

Ответ:

json
```
{
    "id": 1,
    "seller_id": 1,
    "amount": 10000,
    "payment_type": "CARD",
    "transaction_date": "2024-10-20T12:34:56"
}
```

Если id не существует:

json
```
{
    "message": string
}
```


## Аналитика

### 1. Получить самого продуктивного продавца
#### GET /analytics/top-seller

In query:
start_time=LocalDateTime
&
end_time=LocalDateTime

Ответ:

json
```
{
    "id": 1,
    "name": "Mikhail",
    "contact_info": "mvzhzhv@gmail.com",
    "registration_date": "2024-10-20T12:34:56"
}
```

Если за период не нашелся продавец:

json
```
{
    "message": string
}
```

### 2. Получить список продавцов с суммой меньше указанной
#### GET /analytics/looser-sellers
In query:
start_time=LocalDateTime
&
end_time=LocalDateTime
&
amount=long

Ответ:

json
```
[
    {
        "id": 1,
        "seller_id": 1,
        "amount": 10000,
        "payment_type": "CARD",
        "transaction_date": "2024-10-20T12:34:56"
    }
]
```

## Schemas JSON

#### Seller
```
{
    "id": long,
    "name": string,
    "contact_info": string,
    "registration_date": LocalDateTime
}
```

#### Transaction
```
{
    "id": long,
    "seller_id": long,
    "amount": long,
    "payment_type": ["CARD", "CASH", "TRANSFER"],
    "transaction_date": LocalDateTime
}
```

##### Message
```
{
    "message": string
}
```