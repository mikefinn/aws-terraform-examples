output "base_url" {
  value = "${aws_api_gateway_deployment.apiproxylambda.invoke_url}"
}

resource "local_file" "url" {
    content     = "${aws_api_gateway_deployment.apiproxylambda.invoke_url}"
    filename = "url.txt"
}
