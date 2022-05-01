pipeline {
    triggers { issueCommentTrigger('.*test this please.*') }

    options { disableConcurrentBuilds() }

    agent any
    stages {
        stage("Clean project") {
            steps {
                sh './gradlew clean'
            }
        }

        stage('Check Testcode(Debug)') {
            steps {
                sh './gradlew testDebugUnitTest'
            }
        }

        stage('Standard Build(Debug)') {
            steps {
                sh './gradlew assembleDebug'
            }
        }

        stage('SonarQube report to PR') {
            /* environment {
                scannerHome = tool 'SonarQube Scanner'
            } */

            steps {
                reportSonarQubePullRequest()
            }
        }

        stage('SonarQube report to Dashboard') {
            when {
                branch 'develop' // Report to dashboard will be performed on develop branch only.
            }
            /* environment {
                scannerHome = tool 'SonarQube Scanner'
            } */

            steps {
                reportSonarQubeDashboard()
            }
        }

        stage('Code Inspection') {
            parallel {
                stage('Report - Lint coach') {
                    steps {
                        sh './gradlew lintDebug'
                        reportLintCoachPullRequest()
                    }
                }

                stage('Report - basic info') {
                    steps {
                        reportIssueDetailsPullRequest()
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWorkspace()
        }
    }
}

void reportSonarQubePullRequest() {
    script {
        if (env.CHANGE_ID) {
            pullRequestId = env.CHANGE_ID
            echo "Reported to PR: ${pullRequestId}"
        }
    }
}

void reportSonarQubeDashboard() {
    script {
        if (!env.CHANGE_ID) {
            echo "Reported to Dashboard"
        }
    }
}

void reportLintCoachPullRequest() {

}

void reportIssueDetailsPullRequest() {
    script {
        //maybeWriteBtsComment()
        maybeWriteGitCommandComment()
    }
}

void maybeWriteGitCommandComment() {
    echo "Git command assistor : Check and write git command comment"
    if (env.CHANGE_ID) {
        def gitCommandCommentPrefix = "Git hint"
        def prId = env.CHANGE_ID
        def gitCommandCommentBody = gitCommandCommentPrefix + " : 변경사항을 로컬에서 확인하고 싶으면 다음 명령어를 활용해 주세요 " +
                "\r\n`\$ git fetch origin pull/" +
                prId +
                "/head:pr/" +
                prId +
                " && git checkout pr/" +
                prId +
                "`"
        alreadyWrittenCommentId = getCommentIdForAlreadyExist(gitCommandCommentPrefix)
        if (alreadyWrittenCommentId != null) {
            echo "Git hint : Comment edit : $alreadyWrittenCommentId :: $gitCommandCommentBody"
            pullRequest.editComment(alreadyWrittenCommentId, gitCommandCommentBody)
        } else {
            echo "Git hint : Comment write : $gitCommandCommentBody"
            pullRequest.comment(gitCommandCommentBody)
        }
    } else {
        echo "Pass - Git command comment."
    }
}


void cleanWorkspace() {
    cleanWs()
    dir("${env.WORKSPACE}@tmp") {
        deleteDir()
    }
    dir("${env.WORKSPACE}@script") {
        deleteDir()
    }
    dir("${env.WORKSPACE}@script@tmp") {
        deleteDir()
    }
}