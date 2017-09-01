package openmods.igw.api.integration;

/**
 * Functional interface used to execute some code when the relative
 * provider has declared the integration can be loaded.
 *
 * <p>No particular assumption is done on the contract of this
 * interface. Every operation can be performed through this
 * interface.</p>
 *
 * <p>It is allowed for implementations to load platform-specific
 * or environment-specific classes from this interface without
 * checking for their existence: the checks have already been
 * performed before calling this interface's implementation.</p>
 *
 * @since 1.0
 */
public interface IIntegrationExecutor {

    /**
     * Integrates the caller with the software specific for this implementor.
     *
     * <p>There are no restrictions on what the implementor can or can't do
     * when this method is called.</p>
     *
     * <p>If this method completes normally, then integration with the specific
     * software must be complete and no other operations must be performed
     * from the integration provider.</p>
     *
     * <p>If the integration fails to load fot whatever reason, then the method
     * must throw an {@link Exception}. It is suggested to not catch any exceptions
     * in the body of this method for this exact reason.</p>
     *
     * @throws Exception
     *         If the integration fails to execute for whatever reason.
     *
     * @since 1.0
     */
    void integrate() throws Exception;

    /**
     * Executes a series of operations after the integration has been successfully
     * loaded.
     *
     * <p>It is guaranteed that this method is called after {@link #integrate()}
     * iff the integration loaded correctly.</p>
     *
     * <p>Implementations should use this method to perform cleanup or other tasks
     * that must be executed in a secondary moment.</p>
     *
     * @since 1.0
     */
    void andThen();
}
