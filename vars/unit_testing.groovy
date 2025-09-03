def cleanWorkspace() {
    cleanWs()
}

def runTests() {
    sh 'mvn test'
}

def generateReport() {
    sh 'mvn surefire-report:report-only'
}

def archiveReports() {
    junit '**/target/surefire-reports/*.xml'
    archiveArtifacts artifacts: 'target/**/*.jar, target/surefire-reports/*.*', fingerprint: true
}

def sendMail(boolean success, String recipientEmail) {
    if (success) {
        emailext(
            to: recipientEmail,
            subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello,  

Your build has completed successfully.  
Please find the attached HTML test report for details.""",
            attachmentsPattern: 'target/site/surefire-report.html'
        )
    } else {
        emailext(
            to: recipientEmail,
            subject: "Build FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello,  

Your build has failed.  
Logs and test report (if available) are attached for review.""",
            attachLog: true,
            attachmentsPattern: 'target/site/surefire-report.html'
        )
    }
}
