output "db_endpoint" {
  value       = aws_db_instance.mysql.endpoint
  description = "Endpoint de la base de datos MySQL"
}

output "ecs_cluster_name" {
  value       = aws_ecs_cluster.main.name
  description = "Nombre del ECS Cluster"
}