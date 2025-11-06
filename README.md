# Web2Text

Source code for [Web2Text: Deep Structured Boilerplate Removal](https://arxiv.org/abs/1801.02607), full paper at ECIR '18. This fork of [Dalab • Web2Text](https://github.com/dalab/web2text) introduce docker-ready set up and code adjustment for newer engines.

## Introduction

This document only contains topic around the enhancement of the original code. Read [Dalab • Web2Text](https://github.com/dalab/web2text) for further details.

This fork contains the following enhancements:

* Code adjustment for newer engines i.e., TensorFlow 2, Python 3, and Scala 2.11, by utilizing TensorFlow 2 backward compatibility API.
* Docker-ready set up by providing preconfigured Dockerfile.
* New CLI app to execute Web2Text tasks e.g., feature extraction, extracted feature classification and label application.

This fork also come with known limitations:

* Incompatibility with NVidia Cuda. TensorFlow running on CPU instead of utilizing GPU. Possible solution to this is by [installing the NVidia Cuda Toolkit](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/index.html) into the Docker image.

## Installation Prerequisites

* [x] **Mandatory** • Docker engine installed and running.

## Installation

Build Docker image by executing `docker build -t web2text .`

## Usage

1. Run a Docker container and enter the bash terminal by executing `docker run -it --rm -v ./io:/app/io web2text bash`. The `io/` directory mounted so that the input / output files can be read and modified from outside the container and without image rebuild.
2. The CLI app is available through `web2text` command. Try `web2text -h` to read on the CLI app usage, including feature extraction and clasification, and label application.

Here are some examples of the CLI app usage,

* **extract** • `web2text extract io/input.xhtml io/step_1_extracted_features`
* **classify** • `web2text classify io/step_1_extracted_features io/step_2_classified_labels`
* **apply** • `web2text apply io/input.xhtml io/step_2_classified_labels io/step_3_applied_labels`