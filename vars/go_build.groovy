
def compile(String binaryName = 'employee-api', String mainFile = 'main.go') {
    sh 'go mod tidy'
    sh "go build -o ${binaryName} ${mainFile}"
}

def sendMail(boolean success) {
    if (success) {
        emailext(
            to: 'sainiabhishek619@gmail.com',
            subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello Abhishek,  

Your build has completed successfully.  
Code has been compiled successfully."""
        )
    } else {
        emailext(
            to: 'sainiabhishek619@gmail.com',
            subject: "Build FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello Abhishek,  

Your build has failed.  
Please check logs for details."""
        )
    }
}
