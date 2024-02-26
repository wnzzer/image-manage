interface PodsStatus {
    totalPods: number;
    creatingPods: number;
    readyPods: number;
    syncingPods: number;
    syncedPods: number;
    syncingPodNames : string[],
    syncedPodNames : string[],

  }
  interface LoggingEvent {
    timestamp: number; // Long in Java is represented as number in TypeScript
    formattedMessage: string;
    loggerName: string;
    levelString: string;
    threadName: string;
    callerFilename: string;
    callerClass: string;
    callerMethod: string;
    callerLine: string;
    eventId: number; // Long in Java is represented as number in TypeScript
    podName: string;
  }

  interface ResponsePodMetrics {
    podName: string;
    cpuUsage: string[];
    memoryUsage: string[];
  }
  