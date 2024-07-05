package it.eng.dome.search.semantic.domain;

public class Analysis {
	
	public String content;
	public String language;
	public String version;
	public Section[] sections;
	public Paragraphs[] paragraphs;
	public Sentences[] sentences;
	public Phrases[] phrases;
	public Tokens[] tokens;
	public Entities[] entities;
	public MainLemmas[] mainLemmas;
	public MainSyncons[] mainSyncons;
	public Topics[] topics;
	public Knowledge[] knowledge;
	public MainSentences[] mainSentences;
	public MainPhrases[] mainPhrases;
	public Relations[] relations;
	public Sentiment sentiment;
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Section[] getSections() {
		return sections;
	}
	public void setSections(Section[] sections) {
		this.sections = sections;
	}
	public Paragraphs[] getParagraphs() {
		return paragraphs;
	}
	public void setParagraphs(Paragraphs[] paragraphs) {
		this.paragraphs = paragraphs;
	}
	public Sentences[] getSentences() {
		return sentences;
	}
	public void setSentences(Sentences[] sentences) {
		this.sentences = sentences;
	}
	public Phrases[] getPhrases() {
		return phrases;
	}
	public void setPhrases(Phrases[] phrases) {
		this.phrases = phrases;
	}
	public Tokens[] getTokens() {
		return tokens;
	}
	public void setTokens(Tokens[] tokens) {
		this.tokens = tokens;
	}
	public Entities[] getEntities() {
		return entities;
	}
	public void setEntities(Entities[] entities) {
		this.entities = entities;
	}
	public MainLemmas[] getMainLemmas() {
		return mainLemmas;
	}
	public void setMainLemmas(MainLemmas[] mainLemmas) {
		this.mainLemmas = mainLemmas;
	}
	public MainSyncons[] getMainSyncons() {
		return mainSyncons;
	}
	public void setMainSyncons(MainSyncons[] mainSyncons) {
		this.mainSyncons = mainSyncons;
	}
	public Topics[] getTopics() {
		return topics;
	}
	public void setTopics(Topics[] topics) {
		this.topics = topics;
	}
	public Knowledge[] getKnowledge() {
		return knowledge;
	}
	public void setKnowledge(Knowledge[] knowledge) {
		this.knowledge = knowledge;
	}
	public MainSentences[] getMainSentences() {
		return mainSentences;
	}
	public void setMainSentences(MainSentences[] mainSentences) {
		this.mainSentences = mainSentences;
	}
	public MainPhrases[] getMainPhrases() {
		return mainPhrases;
	}
	public void setMainPhrases(MainPhrases[] mainPhrases) {
		this.mainPhrases = mainPhrases;
	}
	public Relations[] getRelations() {
		return relations;
	}
	public void setRelations(Relations[] relations) {
		this.relations = relations;
	}
	public Sentiment getSentiment() {
		return sentiment;
	}
	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}
	

}
