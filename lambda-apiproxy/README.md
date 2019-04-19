# Terraform example: AWS Lambda with API Gateway

This example will use terraform to provision a Java lambda function, with API gateway to expose it as a REST service. The Lambda function merely echoes a specific element from a JSON message back.

#### Build, Deploy, Test

##### Prerequisities
1. Terraform installation with terraform executable in the path
1. Working JDK 1.8 environment
1. Working Maven environment
1. AWS account with an API user


##### AWS Setup
1. Create a new user for terraform to use. User should be a 'Programmatic' user and have appropriate permissions either through group membership or direct policy assigment. For this example, you will need the following policies:
    1. AWSLambdaFullAccess
    1. AmazonAPIGatewayAdministrator
    1. IAMFullAccess
1. Download credentials file (you only get one chance at this) and add them to your AWS credentials file (usually in $HOME/.aws/credentials). It is usually easiest to add a new profile section.
1. Open the main.tf file
    1. Set the profile name to the profile created in previous step
    1. Set the AWS region you wish to use
1. Save

##### Terraform Setup
Prerequisite: terraform installed and in your path
1. In a console window, change to the lambda-apiproxy directory (where this file is located)
1. Initialize the terraform environment. This will interrogate the current directory for any needed modules and download them.

    $ terraform init

##### Build the Java Lambda function project
1. Change directory into the lamba-java-apiproxy directory
1. Build the Java Lambda project using Maven

    $ mvn package

##### Run the terraform script
1. Check the terraform plan. Make sure there are no errors.

     $ terraform plan
1. Provision your resources

     $ terraform apply  
1. The URL of the API Gateway REST service will be written to the output and stored in url.txt file
1. Use your favorite REST testing tool to test your Lamdba API Proxy deployment

     $ curl -X POST -d  "{ \"rqstMessage\" : \"test-input-content\" }" {YOUR_URL}
     $ wget --post-data  "{ \"rqstMessage\" : \"test-input-content\" }" -q -O - {YOUR_URL}
     $ http POST {YOUR_URL} rqstMessage=test-intput-content
    
1. If you make a change to your Java code, simply rebuild and rerun the 'apply' command. The checksum mechanism will pick up the change and deploy it.


##### OPTIONAL: Undeploy your resources
1. Use the terraform 'destroy' command. This will deprovision all your resources.

     $ terraform destroy

     
     
#### Troubleshooting

##### JUnit testing
There is a simple JUnit test case which uses Mockito. It is actually run automatically on maven build.

##### Cloudwatch
Included in this terraform script is permission for the Lambda function to write to the Cloudwatch log. After you have invoked the API, log into the AWS Console, and go to: Cloudwatch > Logs (on the left side). You should see a Log Group named "/aws/lambda/apiproxylambda". Click on that and you can then the latest log stream to see the log output from your function.
