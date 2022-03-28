package kjd.gspro.app.monitor.form;

/**
 * Launch monitor presets pulled from 
 * https://www.globalgolf.com/articles/pro-tip-61/#:~:text=If%20you're%20in%20the,would%20be%2014%2D19%20degrees.
 * 
 * @param ballSpeed
 * @param ballSpinAxis
 * @param ballTotalSpin
 * @param ballLaunchAngle
 * @param ballLaunchDirection
 * @param ballCarryDistance
 * @param clubSpeed
 * @param clubAttackAngle
 * @param clubFaceAngle
 */
public enum FormLaunchPreset {
    DRIVER(167.0f, 0.0f, 2686.0f, 10.9f, 0.0f, 289.0f, 113.0f, -1.3f, 0.0f),
    THREE_WOOD(158.0f, 0.0f, 3655.0f, 9.5f, 0.0f, 243.0f, 107.0f, -2.9f, 0.0f),
    FIVE_WOOD(152.0f, 0.0f, 4350.0f, 10.2f, 0.0f, 230.0f, 103.0f, -3.3f, 0.0f),
    FOUR_IRON(137.0f, 0.0f, 4836.0f, 11.0f, 0.0f, 203.0f, 96.0f, -3.4f, 0.0f),
    FIVE_IRON(130.2f, 0.0f, 5361.0f, 12.1f, 0.0f, 194.0f, 94.0f, -3.7f, 0.0f),
    SIX_IRON(127.0f, 0.0f, 6231.0f, 14.1f, 0.0f, 183.0f, 92.0f, -4.1f, 0.0f),
    SEVEN_IRON(120.0f, 0.0f, 7097.0f, 15.3f, 0.0f, 172.0f, 90.0f, -4.3f, 0.0f),
    EIGHT_IRON(115.0f, 0.0f, 7998.0f, 18.1f, 0.0f, 160.0f, 87.0f, -4.5f, 0.0f),
    NINE_IRON(109.0f, 0.0f, 8647.0f, 20.4f, 0.0f, 148.0f, 85.0f, -4.7f, 0.0f),
    PITCHING_WEDGE(102.0f, 0.0f, 9304.0f, 24.2f, 0.0f, 136.0f, 83.0f, -5.0f, 0.0f),
    GAP_WEDGE(98.0f, 0.0f, 10000.0f, 28.1f, 0.0f, 128.0f, 80.0f, -5.3f, 0.0f);

    
    public final Float ballSpeed;
    public final Float ballSpinAxis;
    public final Float ballTotalSpin;
    public final Float ballLaunchAngle;
    public final Float ballLaunchDirection;
    public final Float ballCarryDistance;
    public final Float clubSpeed;
    public final Float clubAttackAngle;
    public final Float clubFaceAngle;

    private FormLaunchPreset(
        Float ballSpeed,
        Float ballSpinAxis,
        Float ballTotalSpin,
        Float ballLaunchAngle,
        Float ballLaunchDirection,
        Float ballCarryDistance,
        Float clubSpeed,
        Float clubAttackAngle,
        Float clubFaceAngle
    ) {
        this.ballSpeed = ballSpeed;
        this.ballSpinAxis = ballSpinAxis;
        this.ballTotalSpin = ballTotalSpin;
        this.ballLaunchAngle = ballLaunchAngle;
        this.ballLaunchDirection = ballLaunchDirection;
        this.ballCarryDistance = ballCarryDistance;
        this.clubSpeed = clubSpeed;
        this.clubAttackAngle = clubAttackAngle;
        this.clubFaceAngle = clubFaceAngle;
    }
}
