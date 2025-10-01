# Clone your repository
git clone https://github.com/maruthikudadala-k/java-demo.git
cd java-demo

# Create directory structure
mkdir -p src/test/java/com/fleetservice/stepdefinitions
mkdir -p src/main/java/com/fleetservice/pages

# Create the step definition files
# Copy the content from the artifact to these files:
nano src/test/java/com/fleetservice/stepdefinitions/LoginSteps.java
nano src/test/java/com/fleetservice/stepdefinitions/VehicleManagementSteps.java
nano src/test/java/com/fleetservice/stepdefinitions/DriverManagementSteps.java

# Commit and push
git add .
git commit -m "Add step definition files for page generation testing"
git push origin main
