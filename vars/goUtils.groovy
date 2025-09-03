def cleanWorkspace() {
    cleanWs()
}

def compile(String binaryName, String mainFile) {
    sh 'go mod tidy'
    sh "go build -o ${binaryName} ${mainFile}"
}

def sendMail(boolean success, String recipientEmail) {
    if (success) {
        emailext(
            to: recipientEmail,
            subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello,  

Your build has completed successfully.
Code has been compiled successfully.
"""
        )
    } else {
        emailext(
            to: recipientEmail,
            subject: "Build FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello,  

Your build has failed.
"""
        )
    }
}
