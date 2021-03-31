package de.tobiashapp.coinbase.plans

import de.tobiashapp.coinbase.plans.config.AppProperties
import de.tobiashapp.coinbase.plans.runner.PlanConverter
import de.tobiashapp.coinbase.plans.runner.PlanRunner
import mu.KotlinLogging
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

private val LOG = KotlinLogging.logger {}

@Component
class Scheduler(
    private val appProperties: AppProperties,
    private val taskScheduler: TaskScheduler,
    private val planConverter: PlanConverter,
    private val planRunner: PlanRunner,
) {
    @PostConstruct
    fun schedulePlans() {
        if (appProperties.plans == null) {
            return
        }

        for (plan in appProperties.plans) {
            LOG.info("Schedule plan for {}", plan)

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
