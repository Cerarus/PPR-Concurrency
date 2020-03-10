# Subtask 2: Deadlock prevention
## What are the necessary conditions for deadlocks?
- Circular wait: every thread waits for another forming a loop of waiting and blocking
- Mutual exclusion: many threads using one resource at the same time
- Hold and wait: one threads hold on to a resource until it gets another one it is waiting for, blocking the one it is holding for other threads.
- No preemption: Resources are only released voluntarily, no stealing
## Why does the initial solution lead to a deadlock?
  Because all 4 deadlock conditions are met. No philosopher voluntarily releases a fork, they all wait for a second for while holding onto one, creating a circular wait, as all want to access the fork resource.
## Does this strategy resolve the deadlock and why?
  The strategy: all even numbered philosopher start their left fork then right, and all odd philosophers start with their right fork then left. This removes the Circular wait condition.
## Measure the time spent in waiting for fork and compare it to the total runtime?
  
## Can you think of other techniques for deadlock prevention?
- philosophers return the first fork they get when they don't acquire the second fork within a certain time - hold and wait condition removed
- two forks for each philosopher - mutual exclusion removed
- philosophers can steal the second fork from another philosopher - No preemption removed


  
  