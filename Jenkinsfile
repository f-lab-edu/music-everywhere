pipeline {
    agent any


    stages {
        stage('Git Clone') {
            steps {
                // Git 저장소에서 코드 체크아웃
                git branch: 'main', url: 'https://github.com/f-lab-edu/music-everywhere.git'
            }
        }
        stage('Prepare Config') {
            steps {
                script {
                    // Jenkins Credentials에서 파일을 워크스페이스로 복사
                    withCredentials([file(credentialsId: 'music-everywhere-db', variable: 'DB_CONFIG_PATH'), 
                                     file(credentialsId: 'music-everywhere-redis', variable: 'REDIS_CONFIG_PATH'),
                                     file(credentialsId: 'group-service-test', variable: 'GS_TEST')]) {
                        sh '''
                            mkdir -p group-service/src/main/resources
                            mkdir -p group-service/src/test/resources
                            cp $DB_CONFIG_PATH group-service/src/main/resources/application-db.yml
                            cp $REDIS_CONFIG_PATH group-service/src/main/resources/application-redis.yml
                            cp $GS_TEST group-service/src/test/resources/application.properties
                        '''
                    }
                }
            }
        }
        stage('Set Permissions') {
            steps {
                dir('group-service/src/main/resources') {
                    sh 'chmod +rw application-db.yml'
                    sh 'chmod +rw application-redis.yml'
                }
                dir('group-service/src/test/resources') {
                    sh 'chmod +rw application.properties'
                }
                dir('group-service') {
                    sh 'chmod +x gradlew'
                }
            }
        }
        stage('Group Service Build') {
            steps {
                dir('group-service') {
                    // Gradle 빌드 명령어 실행
                    sh './gradlew clean build'
                }
            }
        }
    }
    post {
        success {
            echo 'Build succeeded.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
