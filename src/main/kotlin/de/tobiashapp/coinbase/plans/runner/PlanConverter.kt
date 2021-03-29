package de.tobiashapp.coinbase.plans.runner

import de.tobiashapp.coinbase.plans.config.PlanProperties
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PlanConverter {
    fun convertToPlanExecution(person: PlanProperties): PlanExecution
}
