package de.tobiashapp.coinbase.plans

import de.tobiashapp.coinbase.plans.config.AppProperties
import de.tobiashapp.coinbase.plans.runner.PlanConverter
import de.tobiashapp.coinbase.plans.runner.PlanRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Scheduler(
    private val appProperties: AppProperties,
    private val taskScheduler: TaskScheduler,
    private val planConverter: PlanConverter,
    private val planRunner: PlanRunner,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(Scheduler::class.java)
    }

    @PostConstruct
    fun schedulePlans() {
        if (appProperties.plans == null) {
            return
        }

        for (plan in appProperties.plans) {
            log.info("Schedule plan for {}", plan)

            taskScheduler.schedule(
                {
                    planRunner.run(
                        planConverter.convertToPlanExecution(plan)
                    )
                },
                CronTrigger(plan.cron)
            )
        }
    }
}
