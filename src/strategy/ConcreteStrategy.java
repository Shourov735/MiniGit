package strategy;

/**
 * Strategy context wrapper retained for backwards compatibility; delegates to MergeContext.
 */
public class ConcreteStrategy extends MergeContext {
    public ConcreteStrategy() {
        super(new SourcePreferredStrategy());
    }

    public ConcreteStrategy(Strategy selectedStrategy) {
        super(selectedStrategy);
    }
}
