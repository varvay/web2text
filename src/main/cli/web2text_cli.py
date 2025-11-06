#!/usr/bin/env python3
import subprocess
import argparse
import sys

def run_command(cmd: str):
    print(f"\n$ {cmd}")
    process = subprocess.Popen(
        cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True
    )
    for line in process.stdout:
        print(line, end="")
    process.wait()
    if process.returncode != 0:
        sys.exit(f"❌ Command failed: {cmd}")

def extract(input_html: str, output_base: str):
    """Run the feature extraction step."""
    run_command(f'sbt "runMain ch.ethz.dalab.web2text.ExtractPageFeatures {input_html} {output_base}"')

def classify(feature_base: str, labels_output: str):
    """Run the classification step."""
    run_command(f'python3 src/main/python/main.py classify {feature_base} {labels_output}')

def apply_labels(input_html: str, labels_file: str, output_file: str):
    """Apply predicted labels back to the HTML."""
    run_command(f'sbt "runMain ch.ethz.dalab.web2text.ApplyLabelsToPage {input_html} {labels_file} {output_file}"')

def main():
    parser = argparse.ArgumentParser(
        prog="web2text",
        description="Web2Text CLI — run extraction, classification, or apply labels."
    )
    subparsers = parser.add_subparsers(dest="command", required=True)

    # 1️⃣ Extract command
    extract_parser = subparsers.add_parser("extract", help="Extract page features")
    extract_parser.add_argument("input_html", help="Path to input HTML file")
    extract_parser.add_argument("output_base", help="Output base path for extracted features")

    # 2️⃣ Classify command
    classify_parser = subparsers.add_parser("classify", help="Classify extracted features")
    classify_parser.add_argument("feature_base", help="Input features base (same prefix as extract output)")
    classify_parser.add_argument("labels_output", help="Output path for classified labels")

    # 3️⃣ Apply command
    apply_parser = subparsers.add_parser("apply", help="Apply labels to HTML")
    apply_parser.add_argument("input_html", help="Input HTML file")
    apply_parser.add_argument("labels_file", help="Path to classified labels file (CSV)")
    apply_parser.add_argument("output_file", help="Output HTML file with labels applied")

    args = parser.parse_args()

    if args.command == "extract":
        extract(args.input_html, args.output_base)
    elif args.command == "classify":
        classify(args.feature_base, args.labels_output)
    elif args.command == "apply":
        apply_labels(args.input_html, args.labels_file, args.output_file)
    else:
        parser.print_help()

if __name__ == "__main__":
    main()
