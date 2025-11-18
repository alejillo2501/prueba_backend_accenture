variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Nombre del proyecto"
  type        = string
  default     = "franchise-api"
}

variable "db_name" {
  description = "Nombre de la base de datos"
  type        = string
  default     = "franchise_db"
}

variable "db_username" {
  description = "Usuario de la base de datos"
  type        = string
  default     = "admin"
}

variable "db_password" {
  description = "Password de la base de datos"
  type        = string
  sensitive   = true
}

variable "allowed_cidr" {
  description = "CIDR permitido para acceso a la BD"
  type        = string
  default     = "0.0.0.0/0"
}