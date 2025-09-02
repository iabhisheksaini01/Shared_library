def runZapScan(String targetUrl, String reportName = 'results.html', String zapPath = '/usr/local/bin/zap.sh', int port = 8082) {
    sh """
        echo "Using ZAP at ${zapPath}"

        "${zapPath}" \
            -cmd -port ${port} \
            -quickurl "${targetUrl}" \
            -quickout "\${WORKSPACE}/${reportName}"
    """
}

def archiveReport(String reportName = 'results.html') {
    archiveArtifacts artifacts: reportName
}

def sendMail(boolean success, String reportName = 'results.html', String recipient) {
    if (success) {
        emailext(
            to: recipient,
            subject: "ZAP Scan SUCCESS: ${currentBuild.fullDisplayName}",
            body: "Scan completed successfully. See attached report.",
            attachmentsPattern: reportName
        )
    } else {
        emailext(
            to: recipient,
            subject: "ZAP Scan FAILED: ${currentBuild.fullDisplayName}",
            body: "Scan failed. Check console logs."
        )
    }
}
