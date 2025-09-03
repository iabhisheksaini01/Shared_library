def cleanWorkspace() {
    cleanWs()
}

def runZapScan(String TARGET_URL, String REPORT_NAME, int ZAP_PORT, String ZAP_PATH) {
    sh """
        echo "Using ZAP at ${ZAP_PATH}"

        "${ZAP_PATH}" \
            -cmd -port ${ZAP_PORT} \
            -quickurl "${TARGET_URL}" \
            -quickout "\${WORKSPACE}/${REPORT_NAME}"
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
 
