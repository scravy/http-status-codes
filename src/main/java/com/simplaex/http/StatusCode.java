package com.simplaex.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public enum StatusCode {

    _100(100, "Continue"),
    _101(101, "Switching Protocols"),
    _102(102, "Processing", Category.WebDAV, Category.RFC2518),
    _103(103, "Early Hints", Category.RFC8297),
    _103a(103, "Checkpoint", Category.Unofficial, Category.ResumableRequests),

    _200(200, "OK"),
    _201(201, "Created"),
    _202(202, "Accepted"),
    _203(203, "Non-Authoritative Information", Category.HTTP11),
    _204(204, "No Content"),
    _205(205, "Reset Content"),
    _206(206, "Partial Content", Category.RFC7233),
    _207(207, "Multi-Status", Category.WebDAV, Category.RFC4918),
    _208(208, "Already Reported", Category.WebDAV, Category.RFC5842),
    _218a(218, "This is fine", Category.Unofficial, Category.ApacheWebServer),
    _226(226, "IM Used", Category.RFC3229),

    _300(300, "Multiple Choices"),
    _301(301, "Moved Permanently"),
    _302(302, "Found", "Moved temporarily", Category.Superseded),
    _303(303, "See Other", Category.HTTP11),
    _304(304, "Not Modified"),
    _305(305, "Use Proxy", Category.Obsolete, Category.HTTP11),
    _306(306, "Switch Proxy", Category.Obsolete),
    _307(307, "Temporary Redirect", Category.HTTP11),
    _308(308, "Permanent Redirect", Category.RFC7538),

    _400(400, "Bad Request"),
    _401(401, "Unauthorized", "Not Authorized", Category.RFC7235),
    _402(402, "Payment Required"),
    _403(403, "Forbidden"),
    _404(404, "Not Found"),
    _405(405, "Method Not Allowed"),
    _406(406, "Not Acceptable"),
    _407(407, "Proxy Authentication Required", Category.RFC7235),
    _408(408, "Request timeout"),
    _409(409, "Conflict"),
    _410(410, "Gone"),
    _411(411, "Length Required"),
    _412(412, "Precondition Failed", Category.RFC7232),
    _413(413, "Payload Too Large", Category.RFC7231),
    _414(414, "URI Too Long", Category.RFC7231),
    _415(415, "Unsupported Media Type", Category.RFC7231),
    _416(416, "Range Not Satisfiable", Category.RFC7233),
    _417(417, "Expectation Failed"),
    _418(418, "I'm a teapot", Category.RFC2324, Category.RFC7168),
    _419a(419, "Page Expired", Category.Unofficial, Category.Laravel),
    _420a(420, "Method Failure", Category.Unofficial, Category.Twitter),
    _420b(420, "Enhance Your Calm", Category.Unofficial, Category.Spring),
    _421(421, "Misdirect Request", Category.RFC7540),
    _422(422, "Unprocessable Entity", Category.WebDAV, Category.RFC4918),
    _423(423, "Locked", Category.WebDAV, Category.RFC4918),
    _424(424, "Failed Dependency", Category.WebDAV, Category.RFC4918),
    _425(425, "Too Early", Category.RFC8470),
    _426(426, "Upgrade Required"),
    _428(428, "Precondition Required", Category.RFC6585),
    _429(429, "Too Many Requests", Category.RFC6585),
    _430a(430, "Request Header Fields Too Large", Category.Unofficial, Category.Shopify),
    _431(431, "Request Header Fields Too Large", Category.RFC6585),
    _440a(440, "Login Time-out", Category.Unofficial, Category.MicrosoftInternetInformationServices),
    _444a(444, "No Response", Category.Unofficial, Category.Nginx),
    _449a(449, "Retry With", Category.Unofficial, Category.MicrosoftInternetInformationServices),
    _450a(450, "Blocked by Windows Parental Controls", Category.Unofficial, Category.WindowsParentalControls),
    _451(451, "Unavailable For Legal Reasons", Category.RFC7725),
    _451a(451, "Redirect", Category.Unofficial, Category.MicrosoftInternetInformationServices),
    _460a(460, "", Category.Unofficial, Category.AWSElasticLoadBalacer),
    _463a(463, "", Category.Unofficial, Category.AWSElasticLoadBalacer),
    _494a(494, "Request header too large", Category.Unofficial, Category.Nginx),
    _495a(495, "SSL Certificate Error", Category.Unofficial, Category.Nginx),
    _496a(496, "SSL Certificate Required", Category.Unofficial, Category.Nginx),
    _497a(497, "HTTP Request Sent to HTTPS Port", Category.Unofficial, Category.Nginx),
    _498a(498, "Invalid Token", Category.Unofficial, Category.Esri),
    _499a(499, "Client Closed Request", Category.Unofficial, Category.Nginx),
    _499b(499, "Token Required", Category.Unofficial, Category.Esri),

    _500(500, "Internal Server Error"),
    _501(501, "Not Implemented"),
    _502(502, "Bad Gateway"),
    _503(503, "Service Unavailable"),
    _504(504, "Gateway Timeout"),
    _505(505, "HTTP Version Not Supported"),
    _506(506, "Variant Also Negotiates", Category.RFC2295),
    _507(507, "Insufficient Storage", Category.WebDAV, Category.RFC4918),
    _508(508, "Loop Detected", Category.WebDAV, Category.RFC5842),
    _509a(509, "Bandwidth Limit Exceeded", Category.Unofficial, Category.ApacheWebServer),
    _510(510, "Not Extended", Category.RFC2774),
    _511(511, "Network Authentication Required", Category.RFC6585),
    _520a(520, "Web Server Returned an Unknown Error", Category.Unofficial, Category.Cloudflare),
    _521a(521, "Web Server Is Down", Category.Unofficial, Category.Cloudflare),
    _522a(522, "Connection Timed Out", Category.Unofficial, Category.Cloudflare),
    _523a(523, "Origin Is Unreachable", Category.Unofficial, Category.Cloudflare),
    _524a(524, "A Timeout Occurred", Category.Unofficial, Category.Cloudflare),
    _525a(525, "SSL Handshake Failed", Category.Unofficial, Category.Cloudflare),
    _526a(526, "Invalid SSL Certificate", Category.Unofficial, Category.Cloudflare),
    _527a(527, "Railgun Error", Category.Unofficial, Category.Cloudflare),
    _529a(529, "Site is overloaded", Category.Unofficial, Category.Qualys),
    _530a(530, "Site is frozen", Category.Unofficial, Category.Pantheon),
    _598a(598, "Network read timeout error", Category.Unofficial),

    ;

    private static final SortedSet<StatusCode> officialCodes = new TreeSet<>(Comparator.comparingInt(StatusCode::getCode));
    private static final StatusCode[][] allCodes = new StatusCode[600][];
    private static final Map<String, StatusCode> allCodesByLabel = new HashMap<>(values().length);

    static {
        for (final StatusCode statusCode : StatusCode.values()) {
            if (allCodes[statusCode.code] == null) {
                allCodes[statusCode.code] = new StatusCode[1];
            } else {
                final StatusCode[] codes = allCodes[statusCode.code];
                allCodes[statusCode.code] = new StatusCode[codes.length + 1];
                System.arraycopy(codes, 0, allCodes[statusCode.code], 0, codes.length);
            }
            final StatusCode[] codes = allCodes[statusCode.code];
            codes[codes.length - 1] = statusCode;
            if (statusCode.isOfficial()) {
                officialCodes.add(statusCode);
                swap(codes, 0, codes.length - 1);
            }
            for (final String label : statusCode.getLabels()) {
                allCodesByLabel.put(normalize(label), statusCode);
            }
        }
    }

    private static <T> void swap(final T[] array, final int ix1, final int ix2) {
        final T tmp = array[ix1];
        array[ix1] = array[ix2];
        array[ix2] = tmp;
    }

    private static String normalize(final String name) {
        return Optional.ofNullable(name).map(String::toLowerCase).orElse("").replaceAll("[^a-z]+", "");
    }

    public static Optional<StatusCode> byCode(final int code) {
        if (code >= allCodes.length || code < 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(allCodes[code]).map(codes -> codes[0]);
    }

    public static Optional<StatusCode> byLabel(final String label) {
        return Optional.ofNullable(allCodesByLabel.get(normalize(label)));
    }

    public static SortedSet<StatusCode> all() {
        return Collections.unmodifiableSortedSet(officialCodes);
    }

    public EnumSet<Category> getCategories() {
        return EnumSet.copyOf(categories);
    }

    private final int code;

    @Nonnull
    private final String label;

    @Nonnull
    private final LinkedHashSet<String> labels;

    @Nonnull
    private final EnumSet<Category> categories;

    StatusCode(final int code, @Nonnull final String label, @Nullable final String alternateLabel, final Category... categories) {
        this.code = code;
        this.label = label;
        this.labels = new LinkedHashSet<>();
        labels.add(label);
        if (alternateLabel != null) {
            labels.add(alternateLabel);
        }
        this.categories = EnumSet.noneOf(Category.class);
        Optional.ofNullable(categories).map(Arrays::stream).orElseGet(Stream::empty).forEach(this.categories::add);
    }

    StatusCode(final int code, @Nonnull final String label, final Category... categories) {
        this(code, label, null, categories);
    }

    public String getLabel() {
        return label;
    }

    public Set<String> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public int getCode() {
        return code;
    }

    public boolean isOfficial() {
        return !isUnofficial();
    }

    public boolean isUnofficial() {
        return categories.contains(Category.Unofficial);
    }

    public enum Category {
        Obsolete,
        Superseded,
        WebDAV,
        HTTP11,
        RFC2295,
        RFC2324,
        RFC2518,
        RFC2774,
        RFC3229,
        RFC4918,
        RFC5842,
        RFC6585,
        RFC7168,
        RFC7231,
        RFC7232,
        RFC7233,
        RFC7235,
        RFC7538,
        RFC7540,
        RFC7725,
        RFC8297,
        RFC8470,
        Unofficial,
        ApacheWebServer,
        Laravel,
        Spring,
        Twitter,
        Shopify,
        WindowsParentalControls,
        Esri,
        MicrosoftInternetInformationServices,
        Nginx,
        Cloudflare,
        AWSElasticLoadBalacer,
        Qualys,
        Pantheon,
        ResumableRequests,
    }

    public static final StatusCode CONTINUE = _100;
    public static final StatusCode SWITCHING_PROTOCOLS = _101;
    public static final StatusCode PROCESSING = _102;
    public static final StatusCode EARLY_HINTS = _103;

    public static final StatusCode OK = _200;
    public static final StatusCode CREATED = _201;
    public static final StatusCode ACCEPTED = _202;
    public static final StatusCode NON_AUTHORITATIVE_INFORMATION = _203;
    public static final StatusCode NO_CONTENT = _204;
    public static final StatusCode RESET_CONTENT = _205;
    public static final StatusCode PARTIAL_CONTENT = _206;
    public static final StatusCode MULTI_STATUS = _207;
    public static final StatusCode ALREADY_REPORTED = _208;

    public static final StatusCode THIS_IS_FINE = _218a;
    public static final StatusCode IM_USED = _226;

    public static final StatusCode MULTIPLE_CHOICES = _300;
    public static final StatusCode MOVED_PERMANENTLY = _301;
    public static final StatusCode FOUND = _302;
    public static final StatusCode MOVED_TEMPORARILY = _302;
    public static final StatusCode SEE_OTHER = _303;
    public static final StatusCode NOT_MODIFIED = _304;
    public static final StatusCode USE_PROXY = _305;
    public static final StatusCode SWITCH_PROXY = _306;
    public static final StatusCode TEMPORARY_REDIRECT = _307;
    public static final StatusCode PERMANENT_REDIRECT = _308;

    public static final StatusCode BAD_REQUEST = _400;
    public static final StatusCode UNAUTHORIZED = _401;
    public static final StatusCode NOT_AUTHORIZED = _401;
    public static final StatusCode PAYMENT_REQUIRED = _402;
    public static final StatusCode FORBIDDEN = _403;
    public static final StatusCode NOT_FOUND = _404;
    public static final StatusCode METHOD_NOT_ALLOWED = _405;
    public static final StatusCode NOT_ACCEPTABLE = _406;
    public static final StatusCode PROXY_AUTHENTICATION_REQUIRED = _407;
    public static final StatusCode REQUEST_TIMEOUT = _408;
    public static final StatusCode CONFLICT = _409;
    public static final StatusCode GONE = _410;
    public static final StatusCode LENGTH_REQUIRED = _411;
    public static final StatusCode PRECONDITION_FAILED = _412;
    public static final StatusCode PAYLOAD_TOO_LARGE = _413;
    public static final StatusCode URI_TOO_LONG = _414;
    public static final StatusCode UNSUPPORTED_MEDIA_TYPE = _415;
    public static final StatusCode RANGE_NOT_SATISFIED = _416;
    public static final StatusCode EXPECTATION_FAILED = _417;
    public static final StatusCode I_AM_A_TEAPOT = _418;
    public static final StatusCode MISDIRECT_REQUEST = _421;
    public static final StatusCode UNPROCESSABLE_ENTITY = _422;
    public static final StatusCode LOCKED = _423;
    public static final StatusCode FAILED_DEPENDENCY = _424;
    public static final StatusCode TOO_EARLY = _425;
    public static final StatusCode UPGRADE_REQUIRED = _426;
    public static final StatusCode PRECONDITION_REQUIRED = _428;
    public static final StatusCode TOO_MANY_REQUESTS = _429;
    public static final StatusCode REQUEST_HEADER_FIELDS_TOO_LARGE = _431;
    public static final StatusCode UNAVAILABLE_FOR_LEGAL_REASONS = _451;

    public static final StatusCode INTERNAL_SERVER_ERROR = _500;
    public static final StatusCode NOT_IMPLEMENTED = _501;
    public static final StatusCode BAD_GATEWAY = _502;
    public static final StatusCode SERVICE_UNAVAILABLE = _503;
    public static final StatusCode GATEWAY_TIMEOUT = _504;
    public static final StatusCode HTTP_VERSION_NOT_SUPPORTED = _505;
    public static final StatusCode VARIANT_ALSO_NEGOTIATES = _506;
    public static final StatusCode INSUFFICIENT_STORAGE = _507;
    public static final StatusCode LOOP_DETECTED = _508;
    public static final StatusCode NOT_EXTENDED = _510;
    public static final StatusCode NETWORK_AUTHENTICATION_REQUIRED = _511;

}
