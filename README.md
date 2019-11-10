# Demo Video
Click to watch

[![](http://img.youtube.com/vi/0gFMnc3a_nY/0.jpg)](http://www.youtube.com/watch?v=0gFMnc3a_nY "")

# Get Started
1. Import this project into Android Studio, version: 3.5.1
2. Generate **key.properties** in the project folder 
3. Fill the file with the example below and edit it with your own credentials for Production and Development: 
```
#Pixlee SDK
pixleeAPIKey={API Key for Pixlee SDK}

#PRODUCTION AWS S3
awsAccessKey={Access Key for AWS S3}
awsSecretKey={Secret Key for AWS S3}
awsS3BucketName={S3 bucket name}
awsRegion={region, ex: 'us-east-1' }
s3Domain={domain including bucket name, ex: http://s3.amazonaws.com/sdk-project.pixlee.com/}

#DEVELOPMENT AWS S3
awsAccessKeyForDev={Access Key for AWS S3}
awsSecretKeyForDev={Secret Key for AWS S3}
awsS3BucketNameForDev={S3 bucket name}
awsRegionForDev={region, ex: 'us-east-1' }
s3DomainForDev={domain including bucket name, ex: http://s3.amazonaws.com/sdk-project.pixlee.com/}
```
4. Click this icon below located at the top right in Android Studio.  
<img src="doc/img/gradle_icon.png" width="200">

![make](doc/img/gradle_example.png)

5. Choose one of Build variants
    - Production: prodDebug or prodRelease
    - Development: devDebug, devRelease
    
![make](doc/img/build_variants.png)


6. Run the project on an Android device


# Unit Test
1. Click right button of the mouse on the root of the unit test folder.  
2. Click **Run 'Test in app''**.
3. Check the result if there is any failed test.

![make](doc/img/unit_test.png)