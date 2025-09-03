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

def archiveReports() {
    archiveArtifacts artifacts: 'zap-report.html'
}

def sendMail(boolean success, String recipientEmail) {
    if (success) {
        emailext(
            to: recipientEmail,
            subject: "Build SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello,

Your build has completed successfully.
Please find the attached HTML test report for details.""",
            attachmentsPattern: 'zap-report.html'
        )
    } else {
        emailext(
            to: recipientEmail,
            subject: "Build FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello Abhishek,

The ZAP scan has failed.
Please check the Jenkins console log for more details.""",
            attachLog: true,
            attachmentsPattern: 'results.html'
        )
    }
}
