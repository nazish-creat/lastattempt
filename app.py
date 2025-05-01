from flask import Flask, request, jsonify
from transformers import pipeline

app = Flask(__name__)

# Load Hugging Face summarizer model
summarizer = pipeline("summarization")

@app.route('/summarize', methods=['POST'])
def summarize_text():
    data = request.get_json()
    text = data.get('text', '')
    
    if not text:
        return jsonify({'error': 'No text provided'}), 400

    summary = summarizer(text, max_length=150, min_length=30, do_sample=False)
    return jsonify({'summary': summary[0]['summary_text']})

if __name__ == '__main__':
    app.run(debug=True)