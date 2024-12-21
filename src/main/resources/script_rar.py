import sys
import py7zr

def create_rar(content, output_file):
    try:
        with py7zr.SevenZipFile(output_file, 'w') as archive:
            archive.writestr('processed_content.txt', content)
        print("RAR file created successfully!")
    except Exception as e:
        print(f"Error creating RAR file: {e}")
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python create_rar.py <content> <output_file>")
        sys.exit(1)

    content = sys.argv[1]
    output_file = sys.argv[2]
    create_rar(content, output_file)
