def cleanWorkspace() {
    cleanWs()
}

def compile(String binaryName, String mainFile) {
    sh 'go mod tidy'
    sh "go build -o ${binaryName} ${mainFile}"
}

def checkout(String REPO_URL, String BRANCH_NAME = 'main', String credentialsId = null) {
    git url: REPO_URL, branch: BRANCH_NAME, credentialsId: credentialsId
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
