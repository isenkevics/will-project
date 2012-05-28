echo Building HPUX IA64_32 Motif

# These variables must be set to reflect the compilation context
setenv JAVA_HOME /opt/jdk14101

cd ../org.eclipse.swt/bin/library/
sh build.sh clean
sh build.sh install
