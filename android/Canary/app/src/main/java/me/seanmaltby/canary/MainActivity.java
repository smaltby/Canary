package me.seanmaltby.canary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    private static final int PERMISSIONS_REQUEST = 1;

    private static final String CLIENT_ID = "3e189d315fa64c97abdeaf9f855815e3";
    private static final String REDIRECT_URI = "canary://callback";
    private SpotifyPlayer mPlayer;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        spotifyLogin();
    }

    private void spotifyLogin()
    {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-read-email", "playlist-read-private",
                "playlist-read-collaborative", "playlist-modify-public", "playlist-modify-private",
                "streaming", "user-follow-modify", "user-follow-read", "user-library-read",
                "user-library-modify", "user-read-private", "user-read-birthdate", "user-read-email",
                "user-top-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE)
        {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            mAccessToken = response.getAccessToken();

            switch (response.getType())
            {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d(TAG, "Response contains auth token");
                    Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver()
                    {
                        @Override
                        public void onInitialized(SpotifyPlayer player)
                        {
                            Log.d(TAG, "Initialized player");
                            mPlayer = player;
                            initialize();
                        }

                        @Override
                        public void onError(Throwable throwable)
                        {
                            Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                        }
                    });
                    break;
                case ERROR:
                    Log.d(TAG, "Auth flow returned an error");
                    break;
                default:
                    Log.d(TAG, "Auth flow likely cancelled");
            }
        }
    }

    private void initialize()
    {
        TextView tv = new TextView(this);
        tv.setText("DisplayName = " + getMyDisplayName(mAccessToken));
        setContentView(tv);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public native String getMyDisplayName(String accessToken);
    static
    {
        System.loadLibrary("hello-libs");
    }

}
