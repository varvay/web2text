# ===============================
# Base setup
# ===============================
FROM tensorflow/tensorflow:2.13.0

# Prevent interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# ===============================
# Install system dependencies
# ===============================
RUN apt-get update && apt-get install -y \
    curl \
    zip \
    unzip \
    git \
    build-essential \
    cmake \
    python3 \
    python3-pip \
    python3-venv \
    libboost-all-dev \
    libprotobuf-dev \
    protobuf-compiler \
    libxml2-dev \
    libxslt-dev \
    python3-lxml \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# ===============================
# Install SDKMAN
# ===============================
RUN curl -s "https://get.sdkman.io" | bash
ENV SDKMAN_DIR="/root/.sdkman"
RUN bash -c "source $SDKMAN_DIR/bin/sdkman-init.sh && sdk version"

# ===============================
# Install Java using SDKMAN
# ===============================
RUN bash -c "source $SDKMAN_DIR/bin/sdkman-init.sh && \
  sdk install java 21.0.2-open && \
  sdk install sbt 1.11.7"

# ===============================
# Create app directory
# ===============================
WORKDIR /app

# ===============================
# Copy source code
# ===============================
COPY . /app

# ===============================
# Python environment setup
# ===============================
RUN pip install --upgrade pip setuptools wheel
RUN pip install future
# RUN pip install --no-cache-dir numpy==1.24.3 tensorflow==2.13.0 future

# Install Python dependencies
# (Assuming there's a requirements.txt or setup.py in repo)
# RUN if [ -f requirements.txt ]; then pip install -r requirements.txt; fi

# ===============================
# Build the C++ components
# ===============================
# RUN mkdir -p build && cd build && cmake .. && make -j$(nproc)

# ===============================
# Set environment variables
# ===============================
# ENV PYTHONPATH=/app/src

# ===============================
# Default command
# ===============================
# CMD ["python3", "src/main.py"]
