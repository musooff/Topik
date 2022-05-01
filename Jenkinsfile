pipeline {
    triggers { issueCommentTrigger('.*test this please.*') }

    options { disableConcurrentBuilds() }

    agent any
    stages {
        stage('Stage 1') {
            steps {
                echo 'Hello world!'
            }
        }

        stage('Stage 2') {
            steps {
                echo 'Hello world!'
            }
        }
    }
}