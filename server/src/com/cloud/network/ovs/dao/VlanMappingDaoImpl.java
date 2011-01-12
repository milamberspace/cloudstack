package com.cloud.network.ovs.dao;

import java.util.List;

import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.utils.db.SearchCriteria.Op;

import javax.ejb.Local;

@Local(value = { VlanMappingDao.class })
public class VlanMappingDaoImpl extends GenericDaoBase<VlanMappingVO, Long>
		implements VlanMappingDao {
	protected final SearchBuilder<VlanMappingVO> accountIdSearch;
	protected final SearchBuilder<VlanMappingVO> hostSearch;
	protected final SearchBuilder<VlanMappingVO> accountHostSearch;
	
	public VlanMappingDaoImpl() {
		super();
		accountHostSearch = createSearchBuilder();
		accountHostSearch.and("host_id", accountHostSearch.entity().getHostId(), Op.EQ);
		accountHostSearch.and("account_id", accountHostSearch.entity().getAccountId(), Op.EQ);
        accountHostSearch.done();
        
        accountIdSearch = createSearchBuilder();
        accountIdSearch.and("account_id", accountIdSearch.entity().getAccountId(), Op.EQ);
        accountIdSearch.done();
        
        hostSearch = createSearchBuilder();
        hostSearch.and("host_id", hostSearch.entity().getHostId(), Op.EQ);
		hostSearch.done();
	}
	
	@Override
	public List<VlanMappingVO> listByAccountIdAndHostId(long accountId,
			long hostId) {
		SearchCriteria<VlanMappingVO> sc = accountHostSearch.create();
        sc.setParameters("account_id", accountId);
        sc.setParameters("host_id", hostId);
        return listBy(sc, null);
	}

	@Override
	public List<VlanMappingVO> listByHostId(long hostId) {
		SearchCriteria<VlanMappingVO> sc = hostSearch.create();
        sc.setParameters("host_id", hostId);
        
        return listBy(sc, null);
	}

	@Override
	public List<VlanMappingVO> listByAccountId(long accountId) {
		SearchCriteria<VlanMappingVO> sc = accountIdSearch.create();
        sc.setParameters("account_id", accountId);
        
        return listBy(sc, null);
	}

	@Override
	public VlanMappingVO findByAccountIdAndHostId(long accountId, long hostId) {
		return getByAccountIdAndHostId(accountId, hostId, false);
	}

	@Override
	public List<VlanMappingVO> lockByAccountId(long accountId) {
		SearchCriteria<VlanMappingVO> sc = accountIdSearch.create();
        sc.setParameters("account_id", accountId);
		return lockRows(sc, null, true);
	}

	@Override
	public VlanMappingVO lockByAccountIdAndHostId(long accountId, long hostId) {
		return getByAccountIdAndHostId(accountId, hostId, true);
	}
	
	private VlanMappingVO getByAccountIdAndHostId(long accountId, long hostId, boolean lock) {
		SearchCriteria<VlanMappingVO> sc = accountHostSearch.create();
        sc.setParameters("account_id", accountId);
        sc.setParameters("host_id", hostId);
        
        if (!lock) {
        	return findOneBy(sc);
        } else {
        	return lockOneRandomRow(sc, true);
        }
	}
}
