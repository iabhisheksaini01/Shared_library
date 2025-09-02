// vars/DAST.groovy

// Optional: default call
def call() {
    echo "DAST library loaded"
}

def runZapScan(Map args = [:]) {
    String targetUrl = args.get('targetUrl')
    String reportName = args.get('reportName', 'results.html')
    int port = args.get('port', 8082)
    String zapPath = args.get('zapPath', '/usr/local/bin/zap.sh')

    sh """
        echo "Using ZAP at ${zapPath}"

        "${zapPath}" \
            -cmd -port ${port} \
            -quickurl "${targetUrl}" \
            -quickout "\${WORKSPACE}/${reportName}"
    """
}

def archiveReport(Map args = [:]) {
    String reportName = args.get('reportName', 'results.html')
    archiveArtifacts artifacts: reportName
}

def sendMail(Map args = [:]) {
    boolean success = args.get('success', false)
    String reportName = args.get('reportName', 'results.html')
    String recipient = args.get('recipient')

    if (success) {
        emailext(
            to: recipient,
            subject: "ZAP Scan SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello,  

The ZAP scan has completed successfully.  
HTML report is attached for details.  
""",
            attachmentsPattern: reportName
        )
    } else {
        emailext(
            to: recipient,
            subject: "ZAP Scan FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello,  

The ZAP scan has failed.  
Please check the Jenkins console log for more details.  
"""
        )
    }
}
