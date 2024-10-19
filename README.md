Sehat Scan | an Android app that uses the camera to scan fruits with TensorFlow and recommends juice recipes based on the scanned fruits and user's allergies or health conditions.

[health_postman_v8.json](https://github.com/user-attachments/files/17442681/health_postman_v8.json)Screenshot
![project-sehat-scan](https://github.com/user-attachments/assets/a51a3cbc-e875-4e44-af45-e6793f0a5efe)

Scheme Relation
![relation_scheme](https://github.com/user-attachments/assets/d81e24c6-4366-48de-b116-a98663c3b1ae)

Example Response
[Uploadin{
    "info": {
        "_postman_id": "e148b1fa-4f3e-425e-8c8e-842228662db7",
        "name": "health",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
        "description": ""
    },
    "item": [
        {
            "name": "auth",
            "item": [
                {
                    "name": "login",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/auth/login",
                            "path": [
                                "auth",
                                "login"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "header": [
                            {
                                "key": "Content-Type",
                                "value": "application/json",
                                "disabled": true
                            }
                        ],
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"email\": \"admin@gmail.com\",\n  \"password\": \"admin\"\n}"
                        }
                    }
                },
                {
                    "name": "register",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/auth/register",
                            "path": [
                                "auth",
                                "register"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"name\": \"admin\",\n  \"email\": \"admin@gmail.com\",\n  \"password\": \"admin\"\n}"
                        }
                    }
                },
                {
                    "name": "activation",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/auth/activation",
                            "path": [
                                "auth",
                                "activation"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"token\": \"eyJpZCI6MTUsIm5hbWUiOiJiYWd1cyIsImVtYWlsIjoiYmFndXNidWRpLjI0MTdAZ21haWwuY29tIn0=\"\n}"
                        }
                    }
                },
                {
                    "name": "forgot-password",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/auth/forgot-password",
                            "path": [
                                "auth",
                                "forgot-password"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"email\": \"admin2@gmail.com\"\n}"
                        }
                    }
                },
                {
                    "name": "reset-password",
                    "request": {
                        "method": "PATCH",
                        "url": {
                            "raw": "{{base_url}}/auth/reset-password",
                            "path": [
                                "auth",
                                "reset-password"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"token\": \"eyJ1c2VyX2lkIjo0LCJuYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluMkBnbWFpbC5jb20iLCJyb2xlX2lkIjpudWxsfQ==\",\n  \"password\": \"admin123@\"\n}"
                        }
                    }
                }
            ]
        },
        {
            "name": "disease",
            "item": [
                {
                    "name": "list",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/disease",
                            "path": [
                                "disease"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "create",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/disease",
                            "path": [
                                "disease"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"disease_name\": \"Darah Tinggi\",\n  \"description\": \"Darah tinggi merupakan salah satu penyakit berbahaya.\",\n  \"disease_restrictions\": [\n    {\n      \"nutrition_id\": 1\n    },\n    {\n      \"nutrition_id\": 2\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/disease/2",
                            "path": [
                                "disease",
                                "2"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"disease_name\": \"Diabetes\",\n  \"description\": \"Diabetes merupakan salah satu penyakit berbahaya.\",\n  \"disease_restrictions\": [\n    {\n      \"fruit_nutrition_id\": 3,\n      \"nutrition_id\": 2\n    },\n    {\n      \"fruit_nutrition_id\": 3,\n      \"nutrition_id\": 1\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "delete",
                    "request": {
                        "method": "DELETE",
                        "url": {
                            "raw": "{{base_url}}/disease/1",
                            "path": [
                                "disease",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "find",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/disease/3",
                            "path": [
                                "disease",
                                "3"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "count",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/disease/count",
                            "path": [
                                "disease",
                                "count"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "fruit",
            "item": [
                {
                    "name": "list",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/fruit?keyword=Mangga",
                            "query": [
                                {
                                    "key": "keyword",
                                    "value": "Mangga"
                                }
                            ],
                            "variable": [],
                            "path": [
                                "fruit"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "create",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/fruit",
                            "path": [
                                "fruit"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"fruit_name\": \"Mangga\",\n  \"nutritions\": [\n    {\n      \"nutrition_id\": 1\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/fruit/1",
                            "path": [
                                "fruit",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"fruit_name\": \"Jeruk\",\n  \"nutritions\": [\n    {\n      \"fruit_nutrition_id\": 1,\n      \"nutrition_id\": 1\n    },\n    {\n      \"nutrition_id\": 1\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "delete",
                    "request": {
                        "method": "DELETE",
                        "url": {
                            "raw": "{{base_url}}/fruit/2",
                            "path": [
                                "fruit",
                                "2"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "find",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/fruit/5",
                            "path": [
                                "fruit",
                                "5"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "count",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/fruit/count",
                            "path": [
                                "fruit",
                                "count"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "drink",
            "item": [
                {
                    "name": "list",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/drink",
                            "query": [
                                {
                                    "key": "keyword",
                                    "value": "Jeruk",
                                    "disabled": true
                                }
                            ],
                            "variable": [],
                            "path": [
                                "drink"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "create",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/drink",
                            "path": [
                                "drink"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"drink_name\": \"Jus Mangga\",\n  \"description\": \"Jus Mangga dengan rasa tropis. Mengandung bromelain yang membantu pencernaan dan anti-inflamasi.\",\n  \"ingredients\": [\n    {\n      \"fruit_id\": 1\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "delete",
                    "request": {
                        "method": "DELETE",
                        "url": {
                            "raw": "{{base_url}}/drink/11",
                            "path": [
                                "drink",
                                "11"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "favorite",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/drink/action?type=like",
                            "query": [
                                {
                                    "key": "type",
                                    "value": "like"
                                },
                                {
                                    "key": "type",
                                    "value": "unlike",
                                    "disabled": true
                                }
                            ],
                            "variable": [],
                            "path": [
                                "drink",
                                "action"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"drink_id\": 2\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "find",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/drink/1",
                            "path": [
                                "drink",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "count",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/drink/count",
                            "path": [
                                "drink",
                                "count"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/drink/2",
                            "path": [
                                "drink",
                                "2"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"drink_name\": \"Jus Mangga\",\n  \"description\": \"Jus Mangga dengan rasa tropis. Mengandung bromelain yang membantu pencernaan dan anti-inflamasi.\",\n  \"ingredients\": [\n    {\n      \"drink_detail_id\": 2,\n      \"fruit_id\": 7\n    }\n  ]\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "user",
            "item": [
                {
                    "name": "register disease",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/user/disease",
                            "path": [
                                "user",
                                "disease"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "[2]"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "register allergies",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/user/allergy",
                            "path": [
                                "user",
                                "allergy"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "[7]"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "user",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/user",
                            "path": [
                                "user"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "favorite",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/user/favorite",
                            "path": [
                                "user",
                                "favorite"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update profile",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/user/profile",
                            "path": [
                                "user",
                                "profile"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"name\": \"admin\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "history",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/user/history",
                            "path": [
                                "user",
                                "history"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "count",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/user/count",
                            "path": [
                                "user",
                                "count"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "role",
            "item": [
                {
                    "name": "list",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/role",
                            "path": [
                                "role"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "create",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/role",
                            "path": [
                                "role"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"role_name\": \"user\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/role/1",
                            "path": [
                                "role",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"role_name\": \"admin\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "delete",
                    "request": {
                        "method": "DELETE",
                        "url": {
                            "raw": "{{base_url}}/role/1",
                            "path": [
                                "role",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "predict",
            "item": [
                {
                    "name": "preddict",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/predict",
                            "path": [
                                "predict"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"fruit_name\": \"jeruk\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        },
        {
            "name": "nutrition",
            "item": [
                {
                    "name": "list",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/nutrition",
                            "path": [
                                "nutrition"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "find",
                    "request": {
                        "method": "GET",
                        "url": {
                            "raw": "{{base_url}}/nutrition/1",
                            "path": [
                                "nutrition",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "create",
                    "request": {
                        "method": "POST",
                        "url": {
                            "raw": "{{base_url}}/nutrition",
                            "path": [
                                "nutrition"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"nutrition_name\": \"Vitamin A\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "update",
                    "request": {
                        "method": "PUT",
                        "url": {
                            "raw": "{{base_url}}/nutrition/1",
                            "path": [
                                "nutrition",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "body": {
                            "mode": "raw",
                            "options": {
                                "raw": {
                                    "language": "json"
                                }
                            },
                            "raw": "{\n  \"nutrition_name\": \"Vitamin C\"\n}"
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                },
                {
                    "name": "delete",
                    "request": {
                        "method": "DELETE",
                        "url": {
                            "raw": "{{base_url}}/nutrition/1",
                            "path": [
                                "nutrition",
                                "1"
                            ],
                            "host": [
                                "{{base_url}}"
                            ]
                        },
                        "auth": {
                            "type": "bearer",
                            "bearer": [
                                {
                                    "key": "token",
                                    "value": "{{token}}",
                                    "type": "string"
                                }
                            ]
                        }
                    }
                }
            ]
        }
    ]
}g health_postman_v8.json…]()
