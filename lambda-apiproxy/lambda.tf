variable "lambda_payload_filename" {
  default = "./lambda-java-apiproxy/target/example-api-proxy-lambda-function-0.0.1-SNAPSHOT.jar"
}

variable "lambda_function_handler" {
  default = "com.example.aws.lambda.apiproxy.LambdaFunctionHandler"
}

variable "lambda_runtime" {
  default = "java8"
}

resource "aws_lambda_function" "apiproxylambda" {
  function_name    = "apiproxylambda"
  filename         = "${var.lambda_payload_filename}"
  handler          = "${var.lambda_function_handler}"
  source_code_hash = "${base64sha256(file(var.lambda_payload_filename))}"
  runtime          = "${var.lambda_runtime}"
  role             = "${aws_iam_role.lambda_exec.arn}"
}

# IAM role which dictates what other AWS services the Lambda function
# may access.
resource "aws_iam_role" "lambda_exec" {
  name = "apiproxylambda_lambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

# This is to optionally manage the CloudWatch Log Group for the Lambda Function.
# If skipping this resource configuration, also add "logs:CreateLogGroup" to the IAM policy below.
resource "aws_cloudwatch_log_group" "example" {
  name              = "/aws/lambda/${aws_lambda_function.apiproxylambda.function_name}"
  retention_in_days = 14
}

# See also the following AWS managed policy: AWSLambdaBasicExecutionRole
resource "aws_iam_policy" "lambda_logging" {
  name = "lambda_logging"
  path = "/"
  description = "IAM policy for logging from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role = "${aws_iam_role.lambda_exec.name}"
  policy_arn = "${aws_iam_policy.lambda_logging.arn}"
}
