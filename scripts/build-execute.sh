mvn clean package -DskipTests && \
docker build --platform linux/amd64 -t gcr.io/keen-jigsaw-482516-e6/verno-app . && \
docker push gcr.io/keen-jigsaw-482516-e6/verno-app && \
gcloud run deploy verno-app --image gcr.io/keen-jigsaw-482516-e6/verno-app --region europe-west6