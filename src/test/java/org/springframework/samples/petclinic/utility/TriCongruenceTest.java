package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] != t2arr[0]")
@ClauseDefinition(clause = 'b', def = "t1arr[1] != t2arr[1]")
@ClauseDefinition(clause = 'c', def = "t1arr[2] != t2arr[2]")
@ClauseDefinition(clause = 'd', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'e', def = "t1arr[0] + t1arr[1] < t1arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	// CUTPNF - FFF
	@NearFalsePoint(
		predicate = "a + b + c",
		dnf = "a + b + c",
		implicant = "c",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void sampleTestCUTPNF1() {
		Triangle t1 = new Triangle(7, 3, 3);
		Triangle t2 = new Triangle(7, 3, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	// CUTPNF - TFF
	@UniqueTruePoint(
		predicate = "a + b +c",
		dnf = "a + b + c",
		implicant = "a",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void sampleTestCUTPNF2() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	// CUTPNF - FTF
	@UniqueTruePoint(
		predicate = "a + b +c",
		dnf = "a + b + c",
		implicant = "b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void sampleTestCUTPNF3() {
		Triangle t1 = new Triangle(7, 3, 3);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	// CUTPNF - FFT
	@UniqueTruePoint(
		predicate = "a + b +c",
		dnf = "a + b + c",
		implicant = "c",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void sampleTestCUTPNF4() {
		Triangle t1 = new Triangle(7, 3, 7);
		Triangle t2 = new Triangle(7, 3, 1);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	// CACC - FF
	@CACC(
		predicate = "d + e",
		majorClause = 'd',
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		},
		predicateValue = false
	)
	@Test
	public void sampleTestCACC1() {
		Triangle t1 = new Triangle(2, 3, 1);
		Triangle t2 = new Triangle(2, 3, 1);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	// CACC - FT
	@CACC(
		predicate = "d + e",
		majorClause = 'e',
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		},
		predicateValue = true
	)
	@Test
	public void sampleTestCACC2() {
		Triangle t1 = new Triangle(2, 3, 9);
		Triangle t2 = new Triangle(2, 3, 9);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	// CC - TT ( TF NOT POSSIBLE IN CACC -> CC added to cover d = true)
	@ClauseCoverage(
		predicate = "d + e",
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@Test
	public void sampleTestCC() {
		Triangle t1 = new Triangle(-2, 3, 9);
		Triangle t2 = new Triangle(-2, 3, 9);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * TODO
	 * explain your answer here
	 */
	@UniqueTruePoint( //UTP1 : T T F F F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "ab",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP2 : T T F T F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "ab",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP3 : T T T F F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "ab",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP4 : F F T T F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "cd",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP5 : F T T T F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "cd",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP6 : T F T T F
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "cd",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@UniqueTruePoint( //UTP7 : F F F F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP8 : F F F T T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP9 : F F T F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP10 : T F T F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP11 : T F F F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP12 : T F F T T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP13 : F T T F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP14 : F T F T T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@UniqueTruePoint( //UTP15 : F T F F T
		predicate = "ab + cd +e",
		dnf = "ab + cd + e",
		implicant = "e",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@NearFalsePoint( //with Unique True Point : T T F F F
		predicate = "ab + cd + e",
		dnf = "ab + cd + e",
		implicant = "ab",
		clause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	@NearFalsePoint( //with Unique True Point : T T F F F
		predicate = "ab + cd + e",
		dnf = "ab + cd + e",
		implicant = "ab",
		clause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	@NearFalsePoint( //with Unique True Point : F F T T F
		predicate = "ab + cd + e",
		dnf = "ab + cd + e",
		implicant = "cd",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	@NearFalsePoint( //with Unique True Point : F F T T F
		predicate = "ab + cd + e",
		dnf = "ab + cd + e",
		implicant = "cd",
		clause = 'd',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	@NearFalsePoint( //with Unique True Point : F F F F T
		predicate = "ab + cd + e",
		dnf = "ab + cd + e",
		implicant = "e",
		clause = 'e',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = false),
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false),
		}
	)
	// For CUTPNFP We Have 3 Selected UTP And 5 Founded Near False Point : It means We Will Have 8 Test
	// that 3 of them are UTP, But We Have 15 UTP. So All UTP aren't in our CUTPNFP set
	@Test // For UTP : T T F F F
	public void CUTPNFtest1() {
		boolean areCongruent = questionTwo(true, true, false, false, false);
		Assertions.assertTrue(areCongruent);
	}

	@Test // For UTP : F F T T F
	public void CUTPNFtest2() {
		boolean areCongruent = questionTwo(false, false, true, true, false);
		Assertions.assertTrue(areCongruent);
	}

	@Test // For UTP : F F F F T
	public void CUTPNFtest3() {
		boolean areCongruent = questionTwo(false, false, false, false, true);
		Assertions.assertTrue(areCongruent);
	}

	@Test // For NFP : F T F F F
	public void CUTPNFtest4() {
		boolean areCongruent = questionTwo(false, true, false, false, false);
		Assertions.assertFalse(areCongruent);
	}

	@Test // For NFP : T F F F F
	public void CUTPNFtest5() {
		boolean areCongruent = questionTwo(true, false, false, false, false);
		Assertions.assertFalse(areCongruent);
	}

	@Test // For NFP : F F F T F
	public void CUTPNFtest6() {
		boolean areCongruent = questionTwo(false, false, false, true, false);
		Assertions.assertFalse(areCongruent);
	}

	@Test // For NFP : F F T F F
	public void CUTPNFtest7() {
		boolean areCongruent = questionTwo(false, false, true, false, false);
		Assertions.assertFalse(areCongruent);
	}

	@Test // For NFP : F F F F F
	public void CUTPNFtest8() {
		boolean areCongruent = questionTwo(false, false, false, false, false);
		Assertions.assertFalse(areCongruent);
	}

	// We just have { T T F F F, F F T T F, F F F F T } out of all 15 UTP in our Tests;
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		//boolean predicate = false;
		boolean predicate = a && b || c && d || e;
		return predicate;
	}
}
