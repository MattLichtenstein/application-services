/* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import android.content.Context as MockContext
import org.mozilla.experiments.nimbus.FeaturesInterface
import org.mozilla.experiments.nimbus.MockNimbus
import org.mozilla.experiments.nimbus.internal.FeatureHolder
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val scope = Executors.newWorkStealingPool(5)

val api: FeaturesInterface = MockNimbus("test-feature-holder" to "{}")
val holder = FeatureHolder<String>({ api }, featureId = "test-feature-holder") { "NO CRASH" }

for (i in 0..10000) {
    scope.submit {
        holder.value()
    }
}
for (i in 0..2000) {
    scope.submit {
        holder.value()
    }
}

scope.shutdown()
scope.awaitTermination(2L, TimeUnit.SECONDS)
