package com.tri.ui.test.infrastructur.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import com.tri.persistence.jpql.QueryBuilder;
import com.tri.ui.test.domain.model.sample.Sample;
import com.tri.ui.test.domain.model.sample.SampleRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SampleRepositoryJpa implements SampleRepository {

	@PersistenceContext(unitName = "default")
	EntityManager em;

	@Override
	public List<Sample> findSamples(final int first, final int max,
			final String name, final Long age, List<String> sorting) {
		QueryBuilder builder = querySamples(name, age);
		builder.select.add("s");

		if (sorting != null) {
			for (String sort : sorting) {
				builder.order.add("s." + sort);
			}
		}

		return builder.createQuery(em, Sample.class)
				.setFirstResult((int) first).setMaxResults(max).getResultList();
	}

	@Override
	public long countSamples(final String name, final Long age) {
		QueryBuilder builder = querySamples(name, age);
		builder.select.add("COUNT(s)");

		return builder.createQuery(em, Long.class).getSingleResult();
	}

	QueryBuilder querySamples(final String name, final Long age) {
		QueryBuilder builder = new QueryBuilder();

		builder.from.add("Sample s");
		if (StringUtils.isNotBlank(name)) {
			builder.where.add("UPPER(s.name) like :name");
			builder.setParameter("name", "%" + name.toUpperCase() + "%");
		}
		if (age != null) {
			builder.where.add("s.age = :age");
			builder.setParameter("age", age);
		}

		return builder;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void storeSample(Sample sample) {
		em.persist(sample);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSample(Sample sample) {
		em.remove(sample);
	}

}
