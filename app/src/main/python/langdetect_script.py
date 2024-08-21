from langdetect import detect

def detect_languages(text):
    arabic_text = []
    urdu_text = []

    for line in text.splitlines():
        lang = detect(line)
        if lang == 'ar':
            arabic_text.append(line)
        elif lang == 'ur':
            urdu_text.append(line)

    return "\n".join(arabic_text), "\n".join(urdu_text)