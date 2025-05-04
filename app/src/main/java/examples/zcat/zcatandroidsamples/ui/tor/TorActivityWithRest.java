package examples.zcat.zcatandroidsamples.ui.tor;

import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

public abstract class TorActivityWithRest extends TorActivity {

    @Override
    protected void onTorConnectionStartResult() {
        super.onTorConnectionStartResult();
        if (isTorEnabled && failedCallOnTorNotReady) {
            failedCallOnTorNotReady = false;
            restCalls(WITH_TOR);
        }
    }

    @Override
    protected void onTorConnectionStopResult() {
        super.onTorConnectionStopResult();
    }

    @Override
    protected void onTorConnectionCommonResult() {
        super.onTorConnectionCommonResult();
    }

    @Override
    protected void onTorConnectionFailResult() {
        super.onTorConnectionStopResult();
        torIsNotReady();
    }

    //override
    protected abstract void restCalls(final boolean withTor);

    protected void restCallsTorFail() {
        failedCallOnTorNotReady = true;
    }

    protected void torProtectedRestCalls() {
        if (!isTorEnabled) {
            restCalls(WITHOUT_TOR);
            return;
        }

        if (isTorReady) {
            restCalls(WITH_TOR);
            return;
        }

        if (isSafeContext(context)) {
            restCallsTorFail();
        }
    }

}
