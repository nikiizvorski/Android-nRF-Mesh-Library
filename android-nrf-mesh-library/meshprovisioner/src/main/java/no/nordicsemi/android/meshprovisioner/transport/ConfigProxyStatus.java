/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package no.nordicsemi.android.meshprovisioner.transport;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.opcodes.ConfigMessageOpCodes;

/**
 * To be used as a wrapper class for when creating the ConfigRelayStatus message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ConfigProxyStatus extends ConfigStatusMessage implements Parcelable {

    private static final String TAG = ConfigProxyStatus.class.getSimpleName();
    private static final int OP_CODE = ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_STATUS;

    private int mProxyState;

    /**
     * Constructs a ConfigRelayStatus message.
     *
     * @param node      Mesh node the message originated from
     * @param message   Access message received
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public ConfigProxyStatus(final ProvisionedMeshNode node,
                             @NonNull final AccessMessage message) {
        super(node, message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    private static final Creator<ConfigProxyStatus> CREATOR = new Creator<ConfigProxyStatus>() {
        @Override
        public ConfigProxyStatus createFromParcel(Parcel in) {
            final ProvisionedMeshNode meshNode = (ProvisionedMeshNode) in.readValue(ProvisionedMeshNode.class.getClassLoader());
            final AccessMessage message = (AccessMessage) in.readValue(AccessMessage.class.getClassLoader());
            return new ConfigProxyStatus(meshNode, message);
        }

        @Override
        public ConfigProxyStatus[] newArray(int size) {
            return new ConfigProxyStatus[size];
        }
    };

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    final void parseStatusParameters() {
        final byte[] payload = mMessage.getAccessPdu();
        mProxyState = payload[2];
    }


    /**
     * Returns the current {@link ConfigProxySet.ProxyState} of the node
     */
    @ConfigProxySet.ProxyState
    public int getProxyState() {
        return mProxyState;
    }

    /**
     * Returns true if the proxy feature is currently enabled on the node and false otherwise
     */
    public boolean isProxyFeatureEnabled(){
        return mProxyState == ConfigProxySet.PROXY_FEATURE_ENABLED;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeValue(mNode);
        dest.writeValue(mMessage);
    }
}