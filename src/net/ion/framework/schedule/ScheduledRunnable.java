package net.ion.framework.schedule;

import java.io.Serializable;

/**
 * job을 serialization 하기 위해서 serializable 만 추가로 붙인 runnable
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface ScheduledRunnable extends Runnable, Serializable {
}
