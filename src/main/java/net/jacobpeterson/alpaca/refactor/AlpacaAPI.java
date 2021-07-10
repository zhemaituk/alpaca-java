package net.jacobpeterson.alpaca.refactor;

import net.jacobpeterson.alpaca.model.properties.DataAPIType;
import net.jacobpeterson.alpaca.model.properties.EndpointAPIType;
import net.jacobpeterson.alpaca.refactor.properties.AlpacaProperties;
import net.jacobpeterson.alpaca.refactor.rest.AlpacaClient;
import net.jacobpeterson.alpaca.refactor.rest.endpoint.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link AlpacaAPI} contains several instances of various {@link AbstractEndpoint}s that enable you to interface with
 * Alpaca. You will generally only need one instance of this class in your application. Note that many methods inside
 * the various {@link AbstractEndpoint}s allow <code>null<code/> to be passed in as a parameter if it is optional.
 *
 * @see <a href="https://docs.alpaca.markets/api-documentation/api-v2/">Alpaca API Documentation</a>
 */
public class AlpacaAPI {

    private static final String VERSION_2_PATH_SEGMENT = "v2";

    private final AlpacaClient brokerClient;
    private final AlpacaClient dataClient;
    // Ordering of fields/methods below are analogous to the ordering in the Alpaca documentation
    private final AccountEndpoint accountEndpoint;
    private final MarketDataEndpoint marketDataEndpoint;
    private final OrdersEndpoint ordersEndpoint;
    private final PositionsEndpoint positionsEndpoint;
    private final AssetsEndpoint assetsEndpoint;
    private final WatchlistEndpoint watchlistEndpoint;
    private final CalendarEndpoint calendarEndpoint;
    private final ClockEndpoint clockEndpoint;
    private final AccountConfigurationEndpoint accountConfigurationEndpoint;
    private final AccountActivitiesEndpoint accountActivitiesEndpoint;
    private final PortfolioHistoryEndpoint portfolioHistoryEndpoint;

    /**
     * Instantiates a new {@link AlpacaAPI} using properties specified in <code>alpaca.properties</code> file (or their
     * associated defaults).
     */
    public AlpacaAPI() {
        this(AlpacaProperties.KEY_ID,
                AlpacaProperties.SECRET_KEY,
                AlpacaProperties.ENDPOINT_API_TYPE,
                AlpacaProperties.DATA_API_TYPE);
    }

    /**
     * Instantiates a new {@link AlpacaAPI}.
     *
     * @param keyID  the key ID
     * @param secret the secret
     */
    public AlpacaAPI(String keyID, String secret) {
        this(keyID, secret, null,
                AlpacaProperties.ENDPOINT_API_TYPE,
                AlpacaProperties.DATA_API_TYPE);
    }

    /**
     * Instantiates a new {@link AlpacaAPI}.
     *
     * @param keyID           the key ID
     * @param secret          the secret
     * @param endpointAPIType the {@link EndpointAPIType}
     * @param dataAPIType     the {@link DataAPIType}
     */
    public AlpacaAPI(String keyID, String secret, EndpointAPIType endpointAPIType, DataAPIType dataAPIType) {
        this(keyID, secret, null, endpointAPIType, dataAPIType);
    }

    /**
     * Instantiates a new {@link AlpacaAPI}.
     *
     * @param oAuthToken the OAuth token. Note that the Data API v2 does not work with OAuth tokens.
     */
    public AlpacaAPI(String oAuthToken) {
        this(null, null, oAuthToken,
                AlpacaProperties.ENDPOINT_API_TYPE,
                AlpacaProperties.DATA_API_TYPE);
    }

    /**
     * Instantiates a new {@link AlpacaAPI}.
     *
     * @param keyID           the key ID
     * @param secretKey       the secret key
     * @param oAuthToken      the OAuth token
     * @param endpointAPIType the {@link EndpointAPIType}
     * @param dataAPIType     the {@link DataAPIType}
     */
    public AlpacaAPI(String keyID, String secretKey, String oAuthToken, EndpointAPIType endpointAPIType,
            DataAPIType dataAPIType) {
        checkNotNull(endpointAPIType);
        checkNotNull(dataAPIType);

        String brokerHostSubdomain;
        switch (endpointAPIType) {
            case LIVE:
                brokerHostSubdomain = "live";
                break;
            case PAPER:
                brokerHostSubdomain = "paper-api";
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (oAuthToken == null) {
            brokerClient = new AlpacaClient(keyID, secretKey, brokerHostSubdomain, VERSION_2_PATH_SEGMENT);
            dataClient = new AlpacaClient(keyID, secretKey, "data", VERSION_2_PATH_SEGMENT);
        } else {
            brokerClient = new AlpacaClient(oAuthToken, brokerHostSubdomain, VERSION_2_PATH_SEGMENT);
            dataClient = null;
        }

        accountEndpoint = new AccountEndpoint(brokerClient);
        marketDataEndpoint = dataClient == null ? null : new MarketDataEndpoint(dataClient);
        ordersEndpoint = new OrdersEndpoint(brokerClient);
        positionsEndpoint = new PositionsEndpoint(brokerClient);
        assetsEndpoint = new AssetsEndpoint(brokerClient);
        watchlistEndpoint = new WatchlistEndpoint(brokerClient);
        calendarEndpoint = new CalendarEndpoint(brokerClient);
        clockEndpoint = new ClockEndpoint(brokerClient);
        accountConfigurationEndpoint = new AccountConfigurationEndpoint(brokerClient);
        accountActivitiesEndpoint = new AccountActivitiesEndpoint(brokerClient);
        portfolioHistoryEndpoint = new PortfolioHistoryEndpoint(brokerClient);
    }

    /**
     * @return {@link AccountEndpoint}
     */
    public AccountEndpoint account() {
        return accountEndpoint;
    }

    /**
     * @return the {@link MarketDataEndpoint}
     */
    public MarketDataEndpoint marketData() {
        return marketDataEndpoint;
    }

    /**
     * @return {@link OrdersEndpoint}
     */
    public OrdersEndpoint orders() {
        return ordersEndpoint;
    }

    /**
     * @return {@link PositionsEndpoint}
     */
    public PositionsEndpoint positions() {
        return positionsEndpoint;
    }

    /**
     * @return {@link AssetsEndpoint}
     */
    public AssetsEndpoint assets() {
        return assetsEndpoint;
    }

    /**
     * @return {@link WatchlistEndpoint}
     */
    public WatchlistEndpoint watchlist() {
        return watchlistEndpoint;
    }

    /**
     * @return {@link CalendarEndpoint}
     */
    public CalendarEndpoint calendar() {
        return calendarEndpoint;
    }

    /**
     * @return {@link ClockEndpoint}
     */
    public ClockEndpoint clock() {
        return clockEndpoint;
    }

    /**
     * @return the {@link AccountConfigurationEndpoint}
     */
    public AccountConfigurationEndpoint accountConfiguration() {
        return accountConfigurationEndpoint;
    }

    /**
     * @return the {@link AccountActivitiesEndpoint}
     */
    public AccountActivitiesEndpoint accountActivities() {
        return accountActivitiesEndpoint;
    }

    /**
     * @return the {@link PortfolioHistoryEndpoint}
     */
    public PortfolioHistoryEndpoint portfolioHistory() {
        return portfolioHistoryEndpoint;
    }

    // TODO
    // /**
    //  * Adds the {@link AlpacaStreamListener}. Note that when the first {@link AlpacaStreamListener} is added, the
    //  * Websocket connection is created.
    //  *
    //  * @param streamListener the stream listener
    //  *
    //  * @throws WebsocketException thrown for {@link WebsocketException}s
    //  */
    // public void addAlpacaStreamListener(AlpacaStreamListener streamListener) throws WebsocketException {
    //     alpacaWebSocketClient.addListener(streamListener);
    // }
    //
    // /**
    //  * Removes the {@link AlpacaStreamListener}. Note that when the last {@link AlpacaStreamListener} is removed, the
    //  * Websocket connection is closed.
    //  *
    //  * @param streamListener the {@link AlpacaStreamListener}
    //  *
    //  * @throws WebsocketException thrown for {@link WebsocketException}s
    //  */
    // public void removeAlpacaStreamListener(AlpacaStreamListener streamListener) throws WebsocketException {
    //     alpacaWebSocketClient.removeListener(streamListener);
    // }
    //
    // /**
    //  * Sets the {@link AlpacaWebsocketClient} {@link WebsocketStateListener} to listen to websocket state changes.
    //  *
    //  * @param websocketStateListener the {@link WebsocketStateListener}
    //  */
    // public void setAlpacaStreamWebsocketStateListener(WebsocketStateListener websocketStateListener) {
    //     alpacaWebSocketClient.setWebsocketStateListener(websocketStateListener);
    // }
    //
    // /**
    //  * Adds the {@link MarketDataListener}. Note that when the first {@link AlpacaStreamListener} is added, the
    //  * Websocket connection is created.
    //  *
    //  * @param streamListener the {@link MarketDataListener}
    //  *
    //  * @throws WebsocketException thrown for {@link WebsocketException}s
    //  */
    // public void addMarketDataStreamListener(MarketDataListener streamListener) throws WebsocketException {
    //     marketDataWebSocketClient.addListener(streamListener);
    // }
    //
    // /**
    //  * Removes the {@link MarketDataListener}. Note that when the last {@link MarketDataListener} is removed, the
    //  * Websocket connection is closed.
    //  *
    //  * @param streamListener the {@link MarketDataListener}
    //  *
    //  * @throws WebsocketException thrown for {@link WebsocketException}s
    //  */
    // public void removeMarketDataStreamListener(MarketDataListener streamListener) throws WebsocketException {
    //     marketDataWebSocketClient.removeListener(streamListener);
    // }
    //
    // /**
    //  * Sets the {@link MarketDataWebsocketClient} {@link WebsocketStateListener} to listen to websocket state
    //  changes.
    //  *
    //  * @param websocketStateListener the {@link WebsocketStateListener}
    //  */
    // public void setMarketDataStreamWebsocketStateListener(WebsocketStateListener websocketStateListener) {
    //     marketDataWebSocketClient.setWebsocketStateListener(websocketStateListener);
    // }

    /**
     * Gets {@link #brokerClient}.
     *
     * @return the broker {@link AlpacaClient}
     */
    public AlpacaClient getBrokerClient() {
        return brokerClient;
    }

    /**
     * Gets {@link #dataClient}.
     *
     * @return the data {@link AlpacaClient}
     */
    public AlpacaClient getDataClient() {
        return dataClient;
    }
}