variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "lambda_artifact" {
  type        = string
  description = "Path to lambda zip artifact"
}

variable "ses_sender_email" {
  type        = string
  description = "SES verified sender email"
}
