import pandas as pd

def processStopWords():
    df = pd.read_csv(r'C:\Users\HP\Desktop\CDAC\Minor Project\elections-2019-master\electionsentiment\analysis\tweetprocessor\stopwords.csv')
    stop_words = set(df['Word'])
    return stop_words

if __name__ == '__main__':
    processStopWords()
