export interface TopicMastery {
  topic: string;
  mastery: number;
}

export interface Dashboard {
  userName: string;
  leetcodeVerified: boolean;

  totalAttempts: number;
  questionsSolved: number;
  analyzedAttempts: number;

  strongTopics: TopicMastery[];
  weakTopics: TopicMastery[];
  unexploredTopics: string[];
}
