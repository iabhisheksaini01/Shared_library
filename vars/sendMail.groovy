def call(boolean success, String recipientEmail) {
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
