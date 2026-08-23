export interface AttemptStep {
  eventType: string;
  verdict: string;
  codeDiff: string;

  runtime?: number;
  memory?: number;

  passedTestCases?: number | null;
  totalTestCases?: number | null;

  timestamp: string;
}

export interface AttemptJourney {
  startedAt: string;
  completedAt?: string;
  steps: AttemptStep[];
}

export interface AiInsightsResponse {
  insights: string;
}

export interface QuestionAttempt {
  id: number;

  questionSlug: string;
  title: string;
  difficulty: string;
  language: string;

  runtime: number | null;
  memory: number | null;

  analysisCompleted: boolean;
  analysisRetryCount: number;

  journeyJson: string;

  createdAt: string;

  compileErrors: number;
  logicFailures: number;
  accepted: boolean;
}
