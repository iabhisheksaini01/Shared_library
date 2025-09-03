def cleanWorkspace() {
    cleanWs()
}

def runZapScan(String targetUrl, String reportName, int port, String zapPath) {
    sh """
        echo "Using ZAP at ${zapPath}"

        "${zapPath}" \
            -cmd -port ${port} \
            -quickurl "${targetUrl}" \
            -quickout "\${WORKSPACE}/${reportName}"
    """
}

def archiveReports(String reportFile = 'results.html') {
    archiveArtifacts artifacts: reportFile
}

def sendMail(boolean success, String recipientEmail, String reportName = 'results.html') {
    if (success) {
        emailext(
            to: recipientEmail,
            subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello,  

The ZAP scan has completed successfully.  
HTML report is attached for details.  
""",
            attachmentsPattern: reportName
        )
    } else {
        emailext(
            to: recipientEmail,
            subject: "Build FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello,  

The ZAP scan has failed.  
Please check the Jenkins console log for more details.  
"""
        )
    }
}
